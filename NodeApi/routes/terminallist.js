const express = require("express");
const router = express.Router();
const Terminallist = require("../models/terminallist");

router.post("/", async (req, res) => {
  try {
    const {
      terminalName,
      status,
      createdTimeStamp,
      fiId,
      lastModifiedDate,
      transactionSerialNumber,
      storeTerminalDeletedTimestamp,
      deviceType,
      featurePhoneNumber,
      phoneCountryCode,
      merchantEmail,
      terminalId,
      storeId,
      userId
    } = req.body;

    const newTerminalList = new Terminallist({
      terminalName,
      status,
      createdTimeStamp,
      fiId,
      lastModifiedDate,
      transactionSerialNumber,
      storeTerminalDeletedTimestamp,
      deviceType,
      featurePhoneNumber,
      phoneCountryCode,
      merchantEmail,
      terminalId,
      storeId,
      userId
    });
    const terminalList = await newTerminalList.save();
    const termina_data = {
      terminallist: terminalList
    };

    res.json(termina_data);
  } catch (err) {
    console.error(err.message);
    res.status(500).send("Server Error");
  }
});

module.exports = router;
