import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private REST_API_SERVER = "http://40.114.70.64:3000/dashboard";

  constructor(private httpClient: HttpClient) { }

  data;
  public getDashBoardResult():Observable<any>{
      this.data=this.httpClient.get(this.REST_API_SERVER);
       return this.data;
  }
}
