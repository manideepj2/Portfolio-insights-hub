import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './shared/navbar/navbar';
import { DashboardComponent } from './features/dashboard/dashboard';

@Component({
  selector: 'app-root',
  imports: [Navbar, DashboardComponent, DashboardComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('portfolio-ui');
}
