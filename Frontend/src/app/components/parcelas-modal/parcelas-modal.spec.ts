import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParcelasModal } from './parcelas-modal';

describe('ParcelasModal', () => {
  let component: ParcelasModal;
  let fixture: ComponentFixture<ParcelasModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParcelasModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ParcelasModal);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
