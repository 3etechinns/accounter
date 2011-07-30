package com.vimukti.accounter.developer.api;

import java.io.InputStream;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.vimukti.accounter.api.core.ApiResult;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class XmlSerializationFactory implements ApiSerializationFactory {

	private XmlSerializationFactory instance;
	private XStream stream;

	private XmlSerializationFactory() {
		stream = new XStream();
		initializeStream();
	}

	private void initializeStream() {
		// TODO Auto-generated method stub

	}

	public XmlSerializationFactory getInstance() {
		if (instance == null) {
			instance = new XmlSerializationFactory();
		}
		return instance;
	}

	@Override
	public IAccounterCore deserialize(InputStream inputStream) throws Exception {
		return (IAccounterCore) stream.fromXML(inputStream);
	}

	@Override
	public String serialize(IAccounterCore str) throws Exception {
		return stream.toXML(str);
	}

	@Override
	public List<IAccounterCore> deserializeList(String str) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serializeList(List<IAccounterCore> str) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize(AccounterException ex) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serializeResult(ApiResult apiResult) {
		return stream.toXML(apiResult);
	}

}
