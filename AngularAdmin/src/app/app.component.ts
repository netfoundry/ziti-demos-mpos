import { Component, OnInit } from '@angular/core';
import { DataService } from './data.service';
import { Observable , interval} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'Bank Account Details Dash Board';
  merchantHeadingList=["SNO","Merchant Id","Merchant Name","Merchant Mobile","Merchant Email","Customer Details"];
  customerHeadingsList=["SNO","Customer Mobile","Amount","OTP","Status","Transaction Initiated Date","Transaction Completed Date","Get Difference"];
  merchantsList =[];
  date = new Date();
  merchantsDropDownList =[];
   constructor(private dataService: DataService) { }
  ngOnInit() {
    this.getMerchant();
  } 

  getMerchant(){
    this.dataService.getDashBoardResult().subscribe((responce: any[])=>{      
      this.merchantsList = responce;       
       for(var i=0;i<this.merchantsList.length;i++){
           this.merchantsDropDownList=this.merchantsList[i].merchantId;
           
       }
     })  
  } 
  myClickFunction(cust) {  
    var today = new Date(cust.transactionTime).getTime(); 
    var endDate = new Date(cust.modifiedTime).getTime();
    var days = (endDate - today) / (1000 * 60 * 60 * 24);
    const hours =  (Math.abs(endDate - today) / (1000 * 60 * 60) % 24);
    const minutes =  Math.abs(endDate - today) / (1000 * 60) % 60;
    const seconds = Math.abs(endDate - today) / (1000) % 60; 
    alert("Time Taken  "+Math.round(hours)+" HH : "+Math.round(minutes)+" MM : "+Math.round(seconds)+" SS"); 
  
 }

}
 

setTimeout(() => {
  this.getMerchant();
}, 1000);