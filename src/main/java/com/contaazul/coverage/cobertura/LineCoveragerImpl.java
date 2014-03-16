package com.contaazul.coverage.cobertura;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.contaazul.coverage.cobertura.entity.Clazz;
import com.contaazul.coverage.cobertura.entity.Line;
import com.contaazul.coverage.cobertura.entity.NullLine;

public class LineCoveragerImpl implements LineCoverager {
	private static final Logger logger = LoggerFactory.getLogger( LineCoverager.class );
	private final Clazz clazz;

	public LineCoveragerImpl(Clazz clazz) {
		if (clazz == null)
			throw new CoveragerException( "Clazz cant be null." );
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.contaazul.coverage.cobertura.LineCoverager#getLineCoverage(int)
	 */
	@Override
	public Integer getCoverage(int lineNumber) {
		Line line = findLine( lineNumber );
		logger.debug( "Line.class " + line.getClass().getSimpleName() );
		return getCoverageOf( line );
	}

	private Line findLine(int lineNumber) {
		for (Line line : clazz.getLines())
			if (line.getNumber() == lineNumber)
				return line;
		return new NullLine();
	}

	private int getCoverageOf(Line line) {
		if (line.getConditionCoverage() != null)
			return getConditionCoverage( line );
		return line.getHits() > 0 ? 100 : 0;
	}

	private int getConditionCoverage(Line line) {
		final Pattern regex = Pattern.compile( "%.*" );
		final String value = regex.matcher( line.getConditionCoverage() ).replaceAll( "" );
		logger.debug( "Value parsed " + value );
		return Integer.parseInt( value );
	}
}
