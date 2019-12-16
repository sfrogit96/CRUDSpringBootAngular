import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../employee.service';
import { Employee } from '../employee';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-employee',
  templateUrl: './create-employee.component.html',
  styleUrls: ['./create-employee.component.css']
})
export class CreateEmployeeComponent implements OnInit {

  employee: Employee = new Employee();
  private employeeError: Employee;


  submitted = false;


  constructor(private employeeService: EmployeeService,
  private router: Router) { }
    
  ngOnInit() {
  }

  newEmployee(): void {
    this.submitted = false;
    this.employee = new Employee();
  }

  save(){
    this.employeeService.createEmployee(this.employee)
    .subscribe(
      data => {
        console.log(data);
        this.employeeError = new Employee();
        this.employee = new Employee(); 
        this.submitted = true;
        
      }, 
      error => {
        this.submitted = false;
        this.employeeError = error.error;
        console.log(error);
      }
      );
    
  //  this.gotoList();
  }
  gotoList(){
    this.router.navigate(['/employees']);
  }
  onSubmit(){
    
    this.save();
  }
}
