package com.cts.outward.util;

import com.cts.outward.model.Cheque;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChequeXMLParser {

    public List<Cheque> parse(
            String xmlPath,
            String batchNumber) throws Exception {

        List<Cheque> cheques = new ArrayList<>();

        XMLInputFactory factory =
                XMLInputFactory.newInstance();

        try (FileInputStream inputStream =
                     new FileInputStream(xmlPath)) {

            XMLStreamReader reader =
                    factory.createXMLStreamReader(inputStream);

            Cheque cheque = null;

            while (reader.hasNext()) {

                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {

                    String element =
                            reader.getLocalName();

                    // Start of cheque
                    if ("cheque".equals(element)) {

                     cheque = new Cheque();

                        // Set batch number from the
                        // batch currently being imported
                        cheque.setBatchNumber(
                                batchNumber
                        );

                    } else if (cheque != null) {

                        switch (element) {

                            case "chequeNumber":

                                cheque.setChequeNumber(
                                        reader.getElementText()
                                );

                                break;


                            case "accountNumber":

                                cheque.setAccountNumber(
                                        reader.getElementText()
                                );

                                break;


                            case "drawerName":

                                cheque.setDrawerName(
                                        reader.getElementText()
                                );

                                break;


                            case "amount":

                                String amountText =
                                        reader.getElementText();

                                if (amountText != null &&
                                        !amountText.trim().isEmpty()) {

                                    cheque.setAmount(
                                            new BigDecimal(
                                                    amountText.trim()
                                            )
                                    );
                                }

                                break;


                            case "micrCode":

                                cheque.setMicrCode(
                                        reader.getElementText()
                                );

                                break;


                            case "ifscCode":

                                cheque.setIfscCode(
                                        reader.getElementText()
                                );

                                break;


                            case "status":

                                cheque.setStatus(
                                        reader.getElementText()
                                );

                                break;


                            case "checkedDate":

                                String dateText =
                                        reader.getElementText();

                                if (dateText != null &&
                                        !dateText.trim().isEmpty()) {

                                    cheque.setCheckedDate(
                                            java.time.LocalDate.parse(
                                                    dateText.trim()
                                            )
                                    );
                                }

                                break;


                            case "frontImagePath":

                                cheque.setFrontImagePath(
                                        reader.getElementText()
                                );

                                break;


                            case "backImagePath":

                                cheque.setBackImagePath(
                                        reader.getElementText()
                                );

                                break;


                            default:

                                // Ignore unknown XML elements
                                break;
                        }
                    }

                } else if (
                        event == XMLStreamConstants.END_ELEMENT
                        && "cheque".equals(
                                reader.getLocalName())
                ) {

                    if (cheque != null) {

                        cheques.add(cheque);

                        cheque = null;
                    }
                }
            }

            reader.close();
        }

        return cheques;
    }
}