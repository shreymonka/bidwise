import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PostLoginLandingPageServiceService } from '../../services/post-login-landing-page-service/post-login-landing-page-service.service';
import { AuctionSharedServiceService } from '../../services/auction-shared-service/auction-shared-service.service';

@Component({
  selector: 'app-post-login-landing-page',
  templateUrl: './post-login-landing-page.component.html',
  styleUrl: './post-login-landing-page.component.css'
})
export class PostLoginLandingPageComponent {
  auctions = [
    { title: 'Samsung Earbuds', date: new Date('2024-05-14T14:00:00Z'), image: '/assets/images/avacado.jpeg' },
    { title: 'Avocado Art', date: new Date('2024-05-14T14:00:00Z'), image: '/assets/images/avacado.jpeg' },
    { title: 'Porsche', date: new Date('2024-05-14T14:00:00Z'), image: '/assets/images/avacado.jpeg' }
  ];

  upcomingAuctions: any[] = [];
  firstThreeUpcomingAuctions: any[] = [];

  constructor(private auctionService: PostLoginLandingPageServiceService,private router: Router,    private auctionSharedService: AuctionSharedServiceService  ) { }

  ngOnInit(): void {
    this.fetchUpcomingAuctions();
  }
  fetchUpcomingAuctions(): void {
    this.auctionService.getUpcomingAuctions().subscribe({
      next: (data) => {
        this.upcomingAuctions = data;

        // Get the first three items
        this.firstThreeUpcomingAuctions = data.slice(0, 3); 

        console.log('Upcoming Auctions:',this.fetchUpcomingAuctions);
      },
      error: (error) => {
        console.error('Error fetching upcoming auctions', error);
      }
    });
  }

  bidNow(auction: any): void {
    this.auctionSharedService.changeAuction(auction); 

    this.router.navigate(['/auction'], {
      queryParams: { 
        itemId: auction.itemId 
      }
    });   
      console.log(`Bid now on item ID: ${auction.itemId}`);
  }
  choosePlan(): void {
    this.router.navigate(['/login']); 
  }

// Navigate to the all auctions page
  navigateToAllAuctions(): void {
    this.router.navigate(['/upcoming-all-auctions']); 
  }
}
