package com.mohsin.richlinkpreview;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.webkit.URLUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RichPreview {

    MetaData metaData;
    ResponseListener responseListener;
    String url;
    String userAgent;

    public RichPreview(ResponseListener responseListener) {
        this.responseListener = responseListener;
        metaData = new MetaData();
    }

    public void getPreview(String url) {
        this.url = url;
        metaData.setOriginalUrl(url);
        new getData().execute();
    }

    public void getPreview(String url, String userAgent) {
        this.userAgent = userAgent;
        getPreview(url);
    }

    private class getData extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            try {
                Connection connection = Jsoup.connect(url)
                        .timeout(30 * 1000);
                if (!TextUtils.isEmpty(userAgent)) {
                    connection = connection.userAgent(userAgent);
                }
                doc = connection.get();

                Elements elements = doc.getElementsByTag("meta");

                // getTitle doc.select("meta[property=og:title]")
                String title = doc.select("meta[property=og:title]").attr("content");

                if (title == null || title.isEmpty()) {
                    title = doc.title();
                }
                metaData.setTitle(title);

                //getDescription
                String description = doc.select("meta[name=description]").attr("content");
                if (description.isEmpty() || description == null) {
                    description = doc.select("meta[name=Description]").attr("content");
                }
                if (description.isEmpty() || description == null) {
                    description = doc.select("meta[property=og:description]").attr("content");
                }
                if (description.isEmpty() || description == null) {
                    description = "";
                }
                metaData.setDescription(description);


                // getMediaType
                Elements mediaTypes = doc.select("meta[name=medium]");
                String type = "";
                if (!mediaTypes.isEmpty()) {
                    String media = mediaTypes.attr("content");

                    type = media.equals("image") ? "photo" : media;
                } else {
                    type = doc.select("meta[property=og:type]").attr("content");
                }
                metaData.setMediaType(type);


                //getImages
                Elements imageElements = doc.select("meta[property=og:image]");
                if (!imageElements.isEmpty()) {
                    String image = imageElements.attr("content");
                    if (!image.isEmpty()) {
                        metaData.setImageUrl(resolveURL(url, image));
                    }
                }

                //get image from meta[name=og:image]
                if(metaData.getImageUrl().isEmpty())
                {
                    Elements imageElement = doc.select("meta[name=og:image]");
                    if(!imageElement.isEmpty()) {
                        String image = imageElement.attr("content");
                        if(!image.isEmpty()) {
                            metaData.setImageUrl(resolveURL(url, image));
                        }
                    }
                }

                if(metaData.getImageUrl().isEmpty()) {
                    String src = doc.select("link[rel=image_src]").attr("href");
                    if (!src.isEmpty()) {
                        metaData.setImageUrl(resolveURL(url, src));
                    } else {
                        src = doc.select("link[rel=apple-touch-icon]").attr("href");
                        if (!src.isEmpty()) {
                            metaData.setImageUrl(resolveURL(url, src));
                            metaData.setFavicon(resolveURL(url, src));
                        } else {
                            src = doc.select("link[rel=icon]").attr("href");
                            if (!src.isEmpty()) {
                                metaData.setImageUrl(resolveURL(url, src));
                                metaData.setFavicon(resolveURL(url, src));
                            }
                        }
                    }
                }

                //Favicon
                String src = doc.select("link[rel=apple-touch-icon]").attr("href");
                if (!src.isEmpty()) {
                    metaData.setFavicon(resolveURL(url, src));
                } else {
                    src = doc.select("link[rel=icon]").attr("href");
                    if (!src.isEmpty()) {
                        metaData.setFavicon(resolveURL(url, src));
                    }
                }

                for(Element element : elements) {
                    if(element.hasAttr("property")) {
                        String str_property = element.attr("property").trim();
                        if(str_property.equals("og:url")) {
                            metaData.setUrl(element.attr("content"));
                        }
                        if(str_property.equals("og:site_name")) {
                            metaData.setSiteName(element.attr("content"));
                        }
                    }
                }

                if (metaData.getUrl().equals("") || metaData.getUrl().isEmpty()) {
                    URI uri = null;
                    try {
                        uri = new URI(url);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    if (url == null || uri == null) {
                        metaData.setUrl(url);
                    } else {
                        metaData.setUrl(uri.getHost());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                responseListener.onError(new Exception(
                        "No Html Received from " + url + " Check your Internet " + e.getLocalizedMessage()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            responseListener.onData(metaData);
        }
    }

    private String resolveURL(String url, String part) {
        if (URLUtil.isValidUrl(part)) {
            return part;
        } else {
            URI base_uri = null;
            try {
                base_uri = new URI(url);
                base_uri = base_uri.resolve(part);
                return base_uri.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

}
