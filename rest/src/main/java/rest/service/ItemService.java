package rest.service;

import common.utils.TaotaoResult;

public interface ItemService {
	
	TaotaoResult  getItemBaseInfo(long itemId);
	TaotaoResult  getItemDesc(long itemId);
	
	TaotaoResult getItemParam(long itemId);
	
	
}
