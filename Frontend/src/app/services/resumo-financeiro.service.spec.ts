import { TestBed } from '@angular/core/testing';

import { ResumoFinanceiro } from './resumo-financeiro';

describe('ResumoFinanceiro', () => {
  let service: ResumoFinanceiro;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResumoFinanceiro);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
