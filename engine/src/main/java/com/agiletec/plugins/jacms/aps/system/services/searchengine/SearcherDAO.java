/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 * Data Access Object dedita alle operazioni di ricerca 
 * ad uso del motore di ricerca interno.
 * @author E.Santoboni
 */
public class SearcherDAO implements ISearcherDAO {
	
	/**
	 * Inizializzazione del searcher.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 */
	@Override
	public void init(File dir) {
		this._indexDir = dir;
	}
	
	private IndexSearcher getSearcher() throws IOException {
		FSDirectory directory = new SimpleFSDirectory(_indexDir);
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	private void releaseSearcher(IndexSearcher searcher) throws ApsSystemException {
		try {
			if (searcher != null) {
				searcher.getIndexReader().close();
				//searcher.close();
			}
		} catch (IOException e) {
			throw new ApsSystemException("Errore in chiusura searcher", e);
		}
	}
	
    /**
     * Ricerca una lista di identificativi di contenuto in base 
     * al codice della lingua corrente ed alla parola immessa.
     * @param langCode Il codice della lingua corrente.
     * @param word La parola in base al quale fare la ricerca. Nel caso 
     * venissero inserite stringhe di ricerca del tipo "Venice Amsterdam" 
     * viene considerato come se fosse "Venice OR Amsterdam".
     * @param allowedGroups I gruppi autorizzati alla visualizzazione. Nel caso 
     * che la collezione sia nulla o vuota, la ricerca sarà effettuata su contenuti 
     * referenziati con il gruppo "Ad accesso libero". Nel caso che nella collezione 
     * sia presente il gruppo degli "Amministratori", la ricerca produrrà un'insieme 
     * di identificativi di contenuto non filtrati per gruppo.
     * @return La lista di identificativi contenuto.
     * @throws ApsSystemException
     */
	@Override
    public List<String> searchContentsId(String langCode, 
    		String word, Collection<String> allowedGroups) throws ApsSystemException {
    	List<String> contentsId = new ArrayList<String>();
    	IndexSearcher searcher = null;
    	try {
    		searcher = this.getSearcher();
    		QueryParser parser = new QueryParser(Version.LUCENE_42, langCode, this.getAnalyzer());
    		String queryString = this.createQueryString(langCode, word, allowedGroups);
        	Query query = parser.parse(queryString);
           	int maxSearchLength = 1000;
    		TopDocs topDocs = searcher.search(query, null, maxSearchLength);
    		ScoreDoc[] scoreDoc = topDocs.scoreDocs;
    		if (scoreDoc.length > 0) {
    			for (int index=0; index<scoreDoc.length; index++) {
    				ScoreDoc sDoc = scoreDoc[index];
    				Document doc = searcher.doc(sDoc.doc);
    				contentsId.add(doc.get(IIndexerDAO.CONTENT_ID_FIELD_NAME));
    			}
    		}
    	} catch (IOException e) {
    		throw new ApsSystemException("Errore in estrazione " +
    				"documento in base ad indice", e);
    	} catch (ParseException e) {
    		throw new ApsSystemException("Errore parsing nella ricerca", e);
    	} finally {
    		this.releaseSearcher(searcher);
    	}
    	return contentsId;
    }
    
	private String createQueryString(String langCode, String word, Collection<String> allowedGroups) {
		String queryString = langCode + ":'" + word + "'";
		if (allowedGroups == null) {
			allowedGroups = new HashSet<String>();
		}
		if (!allowedGroups.contains(Group.ADMINS_GROUP_NAME)) {
			if (!allowedGroups.contains(Group.FREE_GROUP_NAME)) {
				allowedGroups.add(Group.FREE_GROUP_NAME);
			}
			queryString += " AND (";
			boolean isFirstGroup = true;
			Iterator<String> iterGroups = allowedGroups.iterator();
			while (iterGroups.hasNext()) {
				String group = iterGroups.next();
				if (!isFirstGroup) queryString += " OR ";
				queryString += IIndexerDAO.CONTENT_GROUP_FIELD_NAME + ":" + group;
				isFirstGroup = false;
			}
			queryString += ")";
		}
		return queryString;
	}
    
    private Analyzer getAnalyzer() {
        return new StandardAnalyzer(Version.LUCENE_42);
    }
	
	@Override
    public void close() {
    	// nothing to do
    }
    
    private File _indexDir;
    
}