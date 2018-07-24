
package org.luwrain.io.api.wikipedia;

public class Wikipedia
{
    private final String lang;

    public Wikipedia(String lang)
    {
	//	NullCheck.notEmpty(lang, "lang");
	this.lang = lang;
    }

    /*
    public Article[] searchArticles(string query) throws WikipediaException
    {
		final LinkedList<Article> res = new LinkedList<Article>();
final Url url = new URL("https://" + URLEncoder.encode(lang) + ".wikipedia.org/w/api.php?action=query&list=search&srsearch=" + URLEncoder.encode(query, "UTF-8") + "&format=xml");
		    final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    final Document document = builder.parse(new InputSource(url.openStream()));
		    final NodeList nodes = document.getElementsByTagName("p");
		    for (int i = 0;i < nodes.getLength();++i)
		    {
			final Node node = nodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE)
			    continue;
			final Element el = (Element)node;
			final NamedNodeMap attr = el.getAttributes();
			final Node title = attr.getNamedItem("title");
			final Node snippet = attr.getNamedItem("snippet");
			if (title != null)
			    res.add(new Article(lang, title.getTextContent(), snippet != null?MlTagStrip.run(snippet.getTextContent()):""));
		    }
return res.toArray(new Page[res.size()]);
		}
		catch(ParserConfigurationException | IOException | SAXException e)
		{
		    throw new WikipediaException(e);
		}
    }
*/
    }
