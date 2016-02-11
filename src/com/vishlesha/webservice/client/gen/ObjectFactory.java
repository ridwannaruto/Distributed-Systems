
package com.vishlesha.webservice.client.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vishlesha.webservice.client.gen package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SearchResponseBean_QNAME = new QName("http://server.webservice.vishlesha.com/", "SearchResponseBean");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vishlesha.webservice.client.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SearchResponseBean }
     * 
     */
    public SearchResponseBean createSearchResponseBean() {
        return new SearchResponseBean();
    }

    /**
     * Create an instance of {@link SearchResponseBean.Files }
     * 
     */
    public SearchResponseBean.Files createSearchResponseBeanFiles() {
        return new SearchResponseBean.Files();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchResponseBean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.webservice.vishlesha.com/", name = "SearchResponseBean")
    public JAXBElement<SearchResponseBean> createSearchResponseBean(SearchResponseBean value) {
        return new JAXBElement<SearchResponseBean>(_SearchResponseBean_QNAME, SearchResponseBean.class, null, value);
    }

}
