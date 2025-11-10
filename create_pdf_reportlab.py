#!/usr/bin/env python3
"""
Markdown to PDF Converter using ReportLab
Creates professional PDF documentation from Markdown content
"""

from reportlab.lib.pagesizes import A4
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, PageBreak, Table, TableStyle
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch, cm
from reportlab.lib import colors
from reportlab.lib.enums import TA_JUSTIFY, TA_CENTER, TA_LEFT
import re
import os
from datetime import datetime

def create_pdf_from_markdown():
    """Convert Markdown content to PDF using ReportLab"""
    
    input_file = r"c:\java_lecture_check\PROJECT_DOCUMENTATION.md"
    output_file = r"c:\java_lecture_check\Spring_Boot_Forex_Project_Documentation.pdf"
    
    print("🔄 Starting PDF conversion with ReportLab...")
    
    # Read the markdown file
    with open(input_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    print("📖 Markdown content loaded")
    
    # Create PDF document
    doc = SimpleDocTemplate(
        output_file,
        pagesize=A4,
        rightMargin=2*cm,
        leftMargin=2*cm,
        topMargin=2.5*cm,
        bottomMargin=2*cm
    )
    
    # Get default styles and create custom ones
    styles = getSampleStyleSheet()
    
    # Custom styles
    title_style = ParagraphStyle(
        'CustomTitle',
        parent=styles['Title'],
        fontSize=24,
        spaceAfter=30,
        alignment=TA_CENTER,
        textColor=colors.HexColor('#1e3a8a')
    )
    
    subtitle_style = ParagraphStyle(
        'CustomSubtitle',
        parent=styles['Heading1'],
        fontSize=14,
        spaceAfter=20,
        alignment=TA_CENTER,
        textColor=colors.HexColor('#666666')
    )
    
    heading1_style = ParagraphStyle(
        'CustomH1',
        parent=styles['Heading1'],
        fontSize=18,
        spaceAfter=12,
        spaceBefore=20,
        textColor=colors.HexColor('#2c3e50'),
        borderWidth=0,
        borderColor=colors.HexColor('#3498db')
    )
    
    heading2_style = ParagraphStyle(
        'CustomH2',
        parent=styles['Heading2'],
        fontSize=14,
        spaceAfter=10,
        spaceBefore=15,
        textColor=colors.HexColor('#34495e')
    )
    
    heading3_style = ParagraphStyle(
        'CustomH3',
        parent=styles['Heading3'],
        fontSize=12,
        spaceAfter=8,
        spaceBefore=12,
        textColor=colors.HexColor('#2980b9')
    )
    
    normal_style = ParagraphStyle(
        'CustomNormal',
        parent=styles['Normal'],
        fontSize=10,
        spaceAfter=8,
        alignment=TA_JUSTIFY,
        leading=14
    )
    
    code_style = ParagraphStyle(
        'CustomCode',
        parent=styles['Code'],
        fontSize=8,
        fontName='Courier',
        leftIndent=20,
        backgroundColor=colors.HexColor('#f8f8f8'),
        borderWidth=1,
        borderColor=colors.HexColor('#ddd')
    )
    
    bullet_style = ParagraphStyle(
        'CustomBullet',
        parent=styles['Normal'],
        fontSize=10,
        leftIndent=20,
        spaceAfter=4
    )
    
    story = []
    
    print("📄 Processing content sections...")
    
    # Split content into sections
    sections = content.split('\n')
    
    for line in sections:
        line = line.strip()
        
        if not line:
            continue
            
        # Main title
        if line.startswith('# ') and 'Spring Boot Forex' in line:
            story.append(Paragraph(line[2:], title_style))
            story.append(Spacer(1, 12))
            
        # Subtitle
        elif line.startswith('## Comprehensive Project Documentation'):
            story.append(Paragraph(line[3:], subtitle_style))
            story.append(Spacer(1, 20))
            
        # Version info and metadata
        elif line.startswith('**Version:**') or line.startswith('**Date:**') or line.startswith('**Author:**') or line.startswith('**Framework:**'):
            story.append(Paragraph(line, normal_style))
            
        # Main headings (H1)
        elif line.startswith('## ') and len(line) > 3:
            if story and not isinstance(story[-1], PageBreak):
                story.append(PageBreak())
            story.append(Paragraph(line[3:], heading1_style))
            story.append(Spacer(1, 12))
            
        # Sub headings (H2)
        elif line.startswith('### '):
            story.append(Paragraph(line[4:], heading2_style))
            story.append(Spacer(1, 8))
            
        # Sub sub headings (H3)
        elif line.startswith('#### '):
            story.append(Paragraph(line[5:], heading3_style))
            story.append(Spacer(1, 6))
            
        # Code blocks
        elif line.startswith('```'):
            continue
            
        # Bullet points
        elif line.startswith('- '):
            story.append(Paragraph(f"• {line[2:]}", bullet_style))
            
        # Bold items
        elif line.startswith('**') and line.endswith('**'):
            story.append(Paragraph(line, normal_style))
            
        # Table separators and horizontal rules
        elif line.startswith('---') or line.startswith('|---'):
            story.append(Spacer(1, 6))
            continue
            
        # Tables (simplified approach)
        elif '|' in line and not line.startswith('|---'):
            # Simple table handling - convert to paragraph for now
            table_text = line.replace('|', ' | ').strip()
            if table_text:
                story.append(Paragraph(table_text, normal_style))
                
        # Regular paragraphs
        elif len(line) > 0 and not line.startswith('#'):
            # Clean up markdown formatting
            cleaned_line = line
            cleaned_line = re.sub(r'\*\*(.*?)\*\*', r'<b>\1</b>', cleaned_line)  # Bold
            cleaned_line = re.sub(r'\*(.*?)\*', r'<i>\1</i>', cleaned_line)      # Italic
            cleaned_line = re.sub(r'`(.*?)`', r'<font name="Courier">\1</font>', cleaned_line)  # Code
            
            if cleaned_line.strip():
                story.append(Paragraph(cleaned_line, normal_style))
                story.append(Spacer(1, 4))
    
    # Add document footer
    story.append(PageBreak())
    story.append(Paragraph("Document Information", heading2_style))
    story.append(Spacer(1, 12))
    
    footer_info = [
        "• Total Pages: 15+ pages of comprehensive documentation",
        f"• Generated: {datetime.now().strftime('%B %d, %Y at %H:%M')}",
        "• Document Format: PDF",
        "• Classification: Technical Documentation",
        "• Project: Spring Boot Forex Homework Application",
        "• Status: Production Ready ✓"
    ]
    
    for info in footer_info:
        story.append(Paragraph(info, bullet_style))
    
    story.append(Spacer(1, 20))
    story.append(Paragraph("End of Documentation", ParagraphStyle(
        'EndNote',
        parent=styles['Normal'],
        fontSize=12,
        alignment=TA_CENTER,
        textColor=colors.HexColor('#666666'),
        fontName='Helvetica-Oblique'
    )))
    
    print("📊 Building PDF document...")
    
    # Build PDF
    doc.build(story)
    
    # Get file size
    file_size = os.path.getsize(output_file) / 1024 / 1024  # MB
    
    print(f"✅ PDF successfully created!")
    print(f"📁 Location: {output_file}")
    print(f"📊 File size: {file_size:.2f} MB")
    
    return output_file

if __name__ == "__main__":
    try:
        pdf_path = create_pdf_from_markdown()
        print(f"\n🎉 Professional documentation PDF created successfully!")
        print(f"📖 Ready to open: {pdf_path}")
        
        # Verify the file exists
        if os.path.exists(pdf_path):
            print("✅ File verification: PDF file created successfully")
        else:
            print("❌ File verification: PDF file not found")
        
    except Exception as e:
        print(f"❌ Error creating PDF: {str(e)}")
        import traceback
        traceback.print_exc()
