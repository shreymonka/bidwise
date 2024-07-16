import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ItemListingServiceService } from '../../services/item-listing-service/item-listing-service.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-item-listing',
  templateUrl: './item-listing.component.html',
  styleUrls: ['./item-listing.component.css']
})
export class ItemListingComponent implements OnInit {

  itemForm: FormGroup;
  categoryName: string = "";

  constructor(
    private fb: FormBuilder,
    private itemService: ItemListingServiceService,
    private route: ActivatedRoute
  ) {
    this.itemForm = this.fb.group({
      itemName: ['', Validators.required],
      itemMaker: ['', Validators.required],
      description: ['', Validators.required],
      pricePaid: ['', [Validators.required, Validators.pattern('^[0-9]+(\.[0-9]{1,2})?$')]],
      currency: ['', Validators.required],
      itemCondition: ['', Validators.required],
      minBidAmount: ['', [Validators.required, Validators.pattern('^[0-9]+(\.[0-9]{1,2})?$')]],
      startDate: ['', Validators.required],
      startTime: ['', Validators.required],
      endDate: ['', Validators.required],
      endTime: ['', Validators.required],
      itemPhoto: ['', Validators.required],
      categoryName: ''
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.categoryName = params['category'];
      this.itemForm.patchValue({
        categoryName: this.categoryName
      });
    });
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.itemForm.patchValue({
        itemPhoto: file
      });
    }
  }

  combineDateTime(date: string, time: string): string {
    return `${date}T${time}:00`;
  }

  onSubmit() {
    if (this.itemForm.valid) {
      const formData = new FormData();
      const formValues = this.itemForm.value;

      // Combine date and time fields into LocalDateTime strings
      const startDateTime = this.combineDateTime(formValues.startDate, formValues.startTime);
      const endDateTime = this.combineDateTime(formValues.endDate, formValues.endTime);

      // Append form values to FormData
      formData.append('itemName', formValues.itemName);
      formData.append('itemMaker', formValues.itemMaker);
      formData.append('description', formValues.description);
      formData.append('pricePaid', formValues.pricePaid);
      formData.append('currency', formValues.currency);
      formData.append('itemCondition', formValues.itemCondition);
      formData.append('minBidAmount', formValues.minBidAmount);
      formData.append('startTime', startDateTime);
      formData.append('endTime', endDateTime);
      formData.append('itemPhoto', formValues.itemPhoto);
      formData.append('categoryName', this.categoryName);

      // Log the form data as JSON
      const formValuesJson = {
        ...formValues,
        startTime: startDateTime,
        endTime: endDateTime
      };
      console.log("Form data as JSON:", JSON.stringify(formValuesJson, null, 2));

      // this.itemService.addItemForAuction(formData).subscribe(response => {
      //   console.log('Item added successfully!', response);
      // }, error => {
      //   console.error('Error adding item', error);
      // });
    }
  }
}
