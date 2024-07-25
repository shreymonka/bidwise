import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuctionItemsServiceService } from '../../services/landing-page-service/auction-items-service.service';
import { AuctionSharedServiceService } from '../../services/auction-shared-service/auction-shared-service.service';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent implements OnInit{
  
  upcomingAuctions: any[] = [];
  firstThreeUpcomingAuctions: any[] = [];


  constructor(
    private auctionService: AuctionItemsServiceService ,
    private router: Router,
    private auctionSharedService: AuctionSharedServiceService, 
  ) { }

  ngOnInit(): void { 
    this.fetchUpcomingAuctions();
  }
  fetchUpcomingAuctions(): void {
  
    this.auctionService.getUpcomingAuctions().subscribe({
      next: (data) => {
        this.upcomingAuctions = data;
        // Get the first three items
        this.firstThreeUpcomingAuctions = data.slice(0, 3); 

        // console.log('Upcoming Auctions:',this.fetchUpcomingAuctions); 
      },
      error: (error) => {
        console.error('Error fetching upcoming auctions', error);
      }
    });
  }

  bidNow(auction: any): void {
    this.auctionSharedService.changeAuction(auction); 

    this.router.navigate(['/login'], {
        queryParams: { 
          returnUrl: `/postLogin`,
          itemId: auction.itemId 
        }
      });  

      console.log(`Bid now on item ID: ${auction.itemId}`);
  }

  choosePlan(): void {
    this.router.navigate(['/login'], { queryParams: { returnUrl: '/pricing' } });
  }
  // Navigate to the pre-login all auctions page
  navigateToAllAuctions(): void {
    this.router.navigate(['/login'], {
      queryParams: { 
        returnUrl: `/postLogin`      }
    }); 
  }

}

