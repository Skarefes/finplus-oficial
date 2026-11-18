import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardTeto } from './card-teto';

describe('CardTeto', () => {
  let component: CardTeto;
  let fixture: ComponentFixture<CardTeto>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardTeto]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardTeto);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
