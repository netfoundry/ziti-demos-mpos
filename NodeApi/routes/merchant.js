const express = require("express");
const router = express.Router();
const Merchant = require("../models/merchant");

router.post("/", async (req, res) => {
  try {
    const {
      lastName,
      firstName,
      cntryPhCode,
      phoneNo,
      status,
      state,
      entityId,
      entityName,
      entityTypeName,
      roleList,
      currency,
      addressLine2,
      address,
      city,
      postalCode,
      country,
      createDate,
      createTimestamp,
      fiId,
      merchantId,
      emailId
    } = req.body;

    const newMerchant = new Merchant({
      lastName,
      firstName,
      cntryPhCode,
      phoneNo,
      status,
      state,
      entityId,
      entityName,
      entityTypeName,
      roleList,
      currency,
      addressLine2,
      address,
      city,
      postalCode,
      country,
      createDate,
      createTimestamp,
      fiId,
      merchantId,
      emailId
    });
    const merchant = await newMerchant.save();
    const resp_data = {
      merchantInfo: merchant
    };
    res.json(resp_data);
  } catch (err) {
    console.error(err.message);
    res.status(500).send("Server Error");
  }
});

module.exports = router;
