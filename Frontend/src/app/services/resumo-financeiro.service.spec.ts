import { TestBed } from '@angular/core/testing';

import { ResumoFinanceiroService } from './resumo-financeiro.service';

describe('ResumoFinanceiroService', () => {
  let service: ResumoFinanceiroService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ResumoFinanceiroService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
