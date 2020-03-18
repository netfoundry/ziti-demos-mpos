const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const terminalList = new Schema({
  terminalId: { type: String },
  createdTimeStamp: { type: Date, default: Date.now },
  status: { type: String },
  fiId: { type: Number },
  fiTerminalId: { type: Number },
  merchantId: { type: Number },
  storeId: { type: Number },
  sequenceNumber: { type: Number },
  userId: { type: String },
  merchantEmail: { type: String },
  lastModifiedDate: { type: Date, default: Date.now },
  transactionSerialNumber: { type: Number },
  storeTerminalDeletedTimestamp: { type: Date, default: Date.now },
  deviceType: { type: String },
  featurePhoneNumber: { type: Number },
  phoneCountryCode: { type: Number }
});

module.exports = mongoose.model("terminalList", terminalList);
