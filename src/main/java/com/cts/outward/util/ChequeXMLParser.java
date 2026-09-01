package com.cts.outward.util;

import com.cts.outward.model.Cheque;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
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

                        // Use batch number from the
                        // batch currently being imported
                        cheque.setBatchNumber(batchNumber);

                    } else if (cheque != null) {

                        switch (element) {

                            case "cheque_number":

                                cheque.setChequeNumber(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "account_number":

                                cheque.setAccountNumber(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "drawer_name":

                                cheque.setDrawerName(
                                        reader.getElementText().trim()
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


                            case "micr_code":

                                cheque.setMicrCode(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "ifsc_code":

                                cheque.setIfscCode(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "status":

                                cheque.setStatus(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "checked_date":

                                String dateText =
                                        reader.getElementText();

                                if (dateText != null &&
                                        !dateText.trim().isEmpty()) {

                                    cheque.setCheckedDate(
                                            LocalDate.parse(
                                                    dateText.trim()
                                            )
                                    );
                                }

                                break;


                            case "front_image_path":

                                cheque.setFrontImagePath(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "back_image_path":

                                cheque.setBackImagePath(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "batch_number":

                                /*
                                 * We intentionally don't use
                                 * the batch number from XML.
                                 *
                                 * The batch number comes from
                                 * the Batch Capture flow.
                                 */
                                reader.getElementText();

                                break;


                            default:

                                // Ignore unknown elements
                                break;
                        }
                    }

                } else if (
                        event == XMLStreamConstants.END_ELEMENT
                        && "cheque".equals(
                                reader.getLocalName())
                ) {

                    if (cheque != null) {

                        // Required field validation
                        if (cheque.getChequeNumber() == null ||
                                cheque.getChequeNumber()
                                        .trim().isEmpty()) {

                            throw new Exception(
                                    "Cheque number is missing in XML."
                            );
                        }

                        if (cheque.getBatchNumber() == null ||
                                cheque.getBatchNumber()
                                        .trim().isEmpty()) {

                            throw new Exception(
                                    "Batch number is missing."
                            );
                        }

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