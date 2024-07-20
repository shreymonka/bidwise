import { Component, OnInit } from '@angular/core';
import { ChartType, ChartOptions, ChartDataset, Color } from 'chart.js'; 

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css',
})
export class UserProfileComponent implements OnInit {
  currentChart: string = 'chart1';

  // Chart 1 - Bar Chart
  public barChartOptions: ChartOptions = {
    responsive: true,
  };
  public barChartLabels: string[] = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;
  public barChartData: ChartDataset<'bar'>[] = [
    { data: [40, 60, 60, 20, 20, 20, 20], label: 'Dataset 1' },
    { data: [50, 100, 120, 80, 60, 60, 80], label: 'Dataset 2' }
  ];
  public barChartColors: Array<any> = [
    { backgroundColor: 'rgba(255, 99, 132, 0.2)', borderColor: 'rgba(255, 99, 132, 1)' },
    { backgroundColor: 'rgba(54, 162, 235, 0.2)', borderColor: 'rgba(54, 162, 235, 1)' }
  ];

  // Chart 2 - Pie Chart
  public pieChartOptions: ChartOptions = {
    responsive: true,
  };
  public pieChartLabels: string[] = ['Red', 'Orange', 'Yellow', 'Green', 'Blue'];
  public pieChartType: ChartType = 'pie';
  public pieChartLegend = true;
  public pieChartData: ChartDataset<'pie'>[] = [
    { data: [30, 50, 70, 20, 40] }
  ];
  public pieChartColors: Array<any> = [
    { backgroundColor: ['rgba(255, 99, 132, 0.2)', 'rgba(255, 159, 64, 0.2)', 'rgba(255, 205, 86, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(54, 162, 235, 0.2)'] }
  ];

  constructor() {}

  ngOnInit(): void { }

  showChart(chartId: string): void {
    console.log(`Switching to ${chartId}`); // Debugging log
    this.currentChart = chartId;  
  }
}
