package fr.stormer3428.voidOpal.util.providers;

public class OMCProviderImpl<T> implements OMCProvider<T>{

	private T data;
	
	protected OMCProviderImpl() {}
	
	public OMCProviderImpl(T data) {
		this.data = data;
	}

	@Override
	public T getData() {
		return data;
	}

	@Override
	public void setData(T data) {
		this.data = data;
	}

}
