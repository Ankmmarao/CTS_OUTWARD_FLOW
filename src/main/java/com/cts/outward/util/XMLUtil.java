package com.cts.outward.util;

import com.cts.outward.model.Batch;
import com.cts.outward.model.Cheque;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class XMLUtil {

    private XMLUtil() {
    }

    public static ParsedBatch parseBatchXML(String filePath)
            throws Exception {

        Batch batch = new Batch();
        List<Cheque> cheques = new ArrayList<>();

        XMLInputFactory factory =
                XMLInputFactory.newFactory();

        // Secure XML configuration
        factory.setProperty(
                XMLInputFactory.SUPPORT_DTD,
                false
        );

        factory.setProperty(
                "javax.xml.stream.isSupportingExternalEntities",
                false
        );

        try (InputStream inputStream =
                     new FileInputStream(filePath)) {

            XMLStreamReader reader =
                    factory.createXMLStreamReader(inputStream);

            Cheque currentCheque = null;

            while (reader.hasNext()) {

                int event = reader.next();

                // =====================================
                // START ELEMENT
                // =====================================

                if (event == XMLStreamConstants.START_ELEMENT) {

                    String tag = reader.getLocalName();

                    // ---------------------------------
                    // Batch
                    // ---------------------------------

                    if ("batch".equalsIgnoreCase(tag)) {
                        continue;
                    }

                    if ("batchNumber".equalsIgnoreCase(tag)) {

                        batch.setBatchNumber(
                                reader.getElementText().trim()
                        );

                    } else if ("branchCode".equalsIgnoreCase(tag)
                            && currentCheque == null) {

                        batch.setBranchCode(
                                reader.getElementText().trim()
                        );

                    } else if ("branchName".equalsIgnoreCase(tag)
                            && currentCheque == null) {

                        batch.setBranchName(
                                reader.getElementText().trim()
                        );

                    } else if ("captureDate".equalsIgnoreCase(tag)
                            && currentCheque == null) {

                        batch.setCaptureDate(
                                LocalDateTime.parse(
                                        reader.getElementText().trim()
                                )
                        );

                    } else if ("createdBy".equalsIgnoreCase(tag)
                            && currentCheque == null) {

                        batch.setCreatedBy(
                                Integer.parseInt(
                                        reader.getElementText().trim()
                                )
                        );

                    } else if ("totalCheque".equalsIgnoreCase(tag)
                            && currentCheque == null) {

                        batch.setTotalCheque(
                                Integer.parseInt(
                                        reader.getElementText().trim()
                                )
                        );

                    } else if ("status".equalsIgnoreCase(tag)
                            && currentCheque == null) {

                        batch.setStatus(
                                reader.getElementText().trim()
                        );
                    }

                    // =====================================
                    // CHEQUE START
                    // =====================================

                    else if ("cheque".equalsIgnoreCase(tag)) {

                        currentCheque = new Cheque();

                        // Link cheque to current batch
                        currentCheque.setBatchNumber(
                                batch.getBatchNumber()
                        );
                    }

                    // =====================================
                    // CHEQUE FIELDS
                    // =====================================

                    else if (currentCheque != null) {

                        switch (tag.toLowerCase()) {

                            case "chequenumber":

                                currentCheque.setChequeNumber(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "accountnumber":

                                currentCheque.setAccountNumber(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "branchcode":

                                currentCheque.setBranchCode(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "payeename":

                                currentCheque.setPayeeName(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "amount":

                                currentCheque.setAmount(
                                        new BigDecimal(
                                                reader.getElementText().trim()
                                        )
                                );

                                break;


                            case "frontimage":

                                currentCheque.setFrontImagePath(
                                        reader.getElementText().trim()
                                );

                                break;


                            case "backimage":

                                currentCheque.setBackImagePath(
                                        reader.getElementText().trim()
                                );

                                break;


                            default:

                                // Ignore unknown fields

                                break;
                        }
                    }
                }

                // =====================================
                // END ELEMENT
                // =====================================

                else if (event == XMLStreamConstants.END_ELEMENT) {

                    String tag = reader.getLocalName();

                    if ("cheque".equalsIgnoreCase(tag)) {

                        if (currentCheque != null) {

                            cheques.add(currentCheque);

                            currentCheque = null;
                        }
                    }
                }
            }

            reader.close();
        }

        // If total cheque is not supplied by XML,
        // calculate it from the actual cheque list.

        if (batch.getTotalCheque() == null) {

            batch.setTotalCheque(
                    cheques.size()
            );
        }

        return new ParsedBatch(
                batch,
                cheques
        );
    }


    // ==================================================
    // Parsed Batch Result
    // ==================================================

    public static class ParsedBatch {

        private final Batch batch;

        private final List<Cheque> cheques;


        public ParsedBatch(
                Batch batch,
                List<Cheque> cheques) {

            this.batch = batch;
            this.cheques = cheques;
        }


        public Batch getBatch() {

            return batch;
        }


        public List<Cheque> getCheques() {

            return cheques;
        }
    }
}