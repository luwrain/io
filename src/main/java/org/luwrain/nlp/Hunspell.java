// * Copyright (C) 2021 Pavel Bakhvalov

package org.luwrain.io.nlp;

import dumonts.hunspell.bindings.HunspellLibrary;
import org.bridj.Pointer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Hunspell
{
private final Pointer<HunspellLibrary.Hunhandle> handle;
  private final Charset charset;

  public Hunspell(Path dictionary, Path affix) 
{
    try {
      Pointer<Byte> aff = Pointer.pointerToCString(affix.toString());
      Pointer<Byte> dic = Pointer.pointerToCString(dictionary.toString());
      handle = HunspellLibrary.Hunspell_create(aff, dic);
      charset = Charset.forName(HunspellLibrary.Hunspell_get_dic_encoding(handle).getCString());
      if (this.handle == null) {
        throw new RuntimeException("Unable to create Hunspell instance");
      }
    } catch (UnsatisfiedLinkError e) {
      throw new RuntimeException("Could not create hunspell instance. Please note that LanguageTool supports only 64-bit platforms " +
          "(Linux, Windows, Mac) and that it requires a 64-bit JVM (Java).", e);
    }
  }

  public boolean spell(String word) {
    if (handle == null) {
      throw new RuntimeException("Attempt to use hunspell instance after closing");
    }
    @SuppressWarnings("unchecked")
    Pointer<Byte> str = (Pointer<Byte>) Pointer.pointerToString(word, Pointer.StringType.C, charset);
    int result = HunspellLibrary.Hunspell_spell(handle, str);
    return result != 0;
  }

  public void add(String word) {
    if (handle == null) {
      throw new RuntimeException("Attempt to use hunspell instance after closing");
    }
    @SuppressWarnings("unchecked")
    Pointer<Byte> str = (Pointer<Byte>) Pointer.pointerToString(word, Pointer.StringType.C, charset);
    HunspellLibrary.Hunspell_add(handle, str);
  }

  public List<String> suggest(String word) {
    // Create pointer to native string
    @SuppressWarnings("unchecked")
    Pointer<Byte> str = (Pointer<Byte>) Pointer.pointerToString(word, Pointer.StringType.C, charset);
    // Create pointer to native string array
    Pointer<Pointer<Pointer<Byte>>> nativeSuggestionArray = Pointer.allocatePointerPointer(Byte.class);
    // Hunspell will allocate the array and fill it with suggestions
    int suggestionCount = HunspellLibrary.Hunspell_suggest(handle, nativeSuggestionArray, str);
    if (suggestionCount == 0) {
      // Return early and don't try to free the array
      return new ArrayList<>();
    }
    // Ask bridj for a `java.util.List` that wraps `nativeSuggestionArray`
    List<Pointer<Byte>> nativeSuggestionList = nativeSuggestionArray.get().validElements(suggestionCount).asList();
    // Convert C Strings to java strings
    List<String> suggestions = nativeSuggestionList.stream().map(p -> p.getStringAtOffset(0, Pointer.StringType.C, charset)).collect(Collectors.toList());

    // We can free the underlying buffer now because Java's `String` owns it's own memory
    HunspellLibrary.Hunspell_free_list(handle, nativeSuggestionArray, suggestionCount);
    return suggestions;
  }

  public void close() throws IOException {
    if (handle != null) {
      HunspellLibrary.Hunspell_destroy(handle);
    }
  }
}
