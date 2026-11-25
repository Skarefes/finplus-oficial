import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Fixos } from './fixos';

describe('Fixos', () => {
  let component: Fixos;
  let fixture: ComponentFixture<Fixos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Fixos]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Fixos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
