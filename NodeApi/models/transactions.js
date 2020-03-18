const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const transactions = new Schema({
  transactionTime: { type: Date, default: Date.now },
  modifiedTime: { type: Date, default: Date.now },
  transactionamount: { type: String },
  mobilenumber: { type: String },
  message: { type: String },
  merchantId: { type: String },
  status: { type: String },
  otp: { type: String }
});

module.exports = mongoose.model("transactions", transactions);
