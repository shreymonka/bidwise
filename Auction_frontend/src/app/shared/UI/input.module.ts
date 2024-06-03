import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InputComponent } from './input/input.component';
import { FooterComponent } from './footer/footer.component';
import { HeroComponent } from './hero/hero.component';



@NgModule({
  declarations: [
    InputComponent,
  ],
  imports: [
    CommonModule
  ],
})
export class InputModule { }
