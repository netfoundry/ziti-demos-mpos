var express =require('express');
var cors=require('cors');
var app =express();
var router = require('express').Router();
var mongoose= require('mongoose');
var bodyParser=require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())
app.use(cors())


const merchantRoutes = require('./routes/merchant');
const userRoutes =require('./routes/users');
const salep2mresponse=require('./routes/salep2mresponse');
const terminalist=require('./routes/terminallist');
const salep2m=require('./routes/salep2m');
const dashboard=require('./routes/dashboard');
const otpres=require('./routes/routeotp');
const userreg=require('./routes/usercreate');
const sale=require('./routes/sale');

app.use('/login',userRoutes);
app.use('/merchant',merchantRoutes);
app.use('/salep2mresponse',salep2mresponse);
app.use('/salep2motpresponse',otpres);
app.use('/salep2m',salep2m);
app.use('/sale',sale);
app.use('/terminalist',terminalist);
app.use('/dashboard',dashboard);
app.use('/dashboard/merchantId',dashboard);
app.use('/userreg',userreg);




mongoose.connect('mongodb://localhost/test', { useUnifiedTopology: true, useNewUrlParser: true  })
        .then(res => console.log("Connected"))
        .catch(err => console.log(err.message));
          
        global.dbo;
          
        var MongoClient = require('mongodb').MongoClient;
        var url = "mongodb://localhost:27017/";
        
        MongoClient.connect(url,{ useUnifiedTopology: true }, function(err, db) {
          if (err) throw err;
          global.dbo = db.db("test");
          
          
        }); 
var port=process.env.PORT || 3000;

app.get('/', function (req, res) {
    res.json({
        status: 'API Its Working',
        message: 'Welcome to RESTHub crafted with love!'
    });
});

app.listen(port, function () {
    console.log("Running RestHub on port " + port);
});

module.exports=router;
