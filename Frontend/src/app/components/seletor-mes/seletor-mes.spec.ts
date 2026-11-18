import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SeletorMes } from './seletor-mes';

describe('SeletorMes', () => {
  let component: SeletorMes;
  let fixture: ComponentFixture<SeletorMes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SeletorMes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SeletorMes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
