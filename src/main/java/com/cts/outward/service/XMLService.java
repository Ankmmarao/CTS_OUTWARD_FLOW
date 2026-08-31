package com.cts.outward.service;

import java.util.List;

import com.cts.outward.model.Cheque;
import com.cts.outward.repository.ChequeRepository;
import com.cts.outward.util.ChequeXMLParser;

public class XMLService {

    private final ChequeXMLParser parser;
    private final ChequeRepository chequeRepository;

    public XMLService() {

        parser = new ChequeXMLParser();

        chequeRepository = new ChequeRepository();
    }

    public int importXML(
            String xmlPath,
            String batchNumber) throws Exception {

        List<Cheque> cheques =
                (List<Cheque>) parser.parse(xmlPath, batchNumber);

        for (Cheque cheque : cheques) {

            chequeRepository.save(cheque);
        }

        return cheques.size();
    }
}