package com.contaazul.coverage.pullrequest.cobertura;

public class IgnoredCobertura implements Cobertura {

	@Override
	public void incrementCoverage(double coverage) {
	}

	@Override
	public double getCoverage() {
		return 0.0;
	}

	@Override
	public void incrementCoverage(int line, int coverage) {
	}

	@Override
	public int getLastLine() {
		return -1;
	}

	@Override
	public boolean isLowerThan(int allowed) {
		return true;
	}

	@Override
	public String toString() {
		return "IgnoredCobertura []";
	}
}