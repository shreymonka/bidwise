import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-post-login-landing-page',
  templateUrl: './post-login-landing-page.component.html',
  styleUrl: './post-login-landing-page.component.css'
})
export class PostLoginLandingPageComponent {
  auctions = [
    {
      title: 'Samsung Earbuds',
      date: new Date('2024-05-14T14:00:00Z'),
      image: 'path/to/samsung-earbuds.jpg'
    },
    {
      title: 'Avocado Art',
      date: new Date('2024-05-14T14:00:00Z'),
      image: 'path/to/avocado-art.jpg'
    },
    {
      title: 'Porsche',
      date: new Date('2024-05-14T14:00:00Z'),
      image: 'path/to/porsche.jpg'
    }
  ];

  constructor(private router: Router) { }

  ngOnInit(): void { }

  bidNow(auction: any): void {
    // Implement the bid now functionality here
    console.log(`Bid now on ${auction.title}`);
  }
  choosePlan(): void {
    this.router.navigate(['/login']); // Navigate to login page
  }
}
