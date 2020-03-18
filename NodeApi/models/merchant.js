const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const merchant = new Schema({
  lastName: { type: String },
  firstName: { type: String },
  cntryPhCode: { type: String },
  phoneNo: { type: String },
  status: { type: String },
  state: { type: String },
  entityId: { type: String },
  entityName: { type: String },
  entityTypeName: { type: String },
  roleList: { type: String },
  currency: { type: String },
  addressLine2: { type: String },
  address: { type: String },
  city: { type: String },
  postalCode: { type: String },
  country: { type: String },
  createDate: { type: Date },
  createTimestamp: { type: Date },
  fiType: { type: String },
  fiId: { type: String },
  merchantId: { type: String },
  emailId: { type: String }
});

module.exports = mongoose.model("merchant", merchant);
