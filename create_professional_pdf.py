#!/usr/bin/env python3
"""
Professional PDF Generator for Spring Boot Forex Project Documentation
Converts Markdown to a well-formatted 15-page PDF document
"""

import os
import re
from reportlab.lib.pagesizes import letter, A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, PageBreak, Table, TableStyle
from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_JUSTIFY
from reportlab.pdfgen import canvas
import markdown
from datetime import datetime

class NumberedCanvas(canvas.Canvas):
    """Custom canvas for page numbering and headers"""
    def __init__(self, *args, **kwargs):
        canvas.Canvas.__init__(self, *args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        """Add page numbers to all pages"""
        num_pages = len(self._saved_page_states)
        for (page_num, page_state) in enumerate(self._saved_page_states):
            self.__dict__.update(page_state)
            self.draw_page_number(page_num + 1, num_pages)
            canvas.Canvas.showPage(self)
        canvas.Canvas.save(self)

    def draw_page_number(self, page_num, num_pages):
        """Draw page number and header on each page"""
        self.setFont("Helvetica", 9)
        # Page number at bottom center
        self.drawCentredText(A4[0] / 2.0, 0.75 * inch, f"Page {page_num} of {num_pages}")
        # Header
        if page_num > 1:  # No header on title page
            self.drawString(72, A4[1] - 0.75 * inch, "Spring Boot Forex Homework Application - Documentation")
            self.line(72, A4[1] - 0.85 * inch, A4[0] - 72, A4[1] - 0.85 * inch)

def create_pdf_from_markdown(md_file_path, output_pdf_path):
    """Convert Markdown file to professional PDF"""
    
    # Read the markdown file
    with open(md_file_path, 'r', encoding='utf-8') as file:
        md_content = file.read()
    
    # Remove HTML comments
    md_content = re.sub(r'<!-- .* -->', '', md_content)
    
    # Create PDF document
    doc = SimpleDocTemplate(
        output_pdf_path,
        pagesize=A4,
        rightMargin=72,
        leftMargin=72,
        topMargin=inch,
        bottomMargin=inch,
        canvasmaker=NumberedCanvas
    )
    
    # Get styles
    styles = getSampleStyleSheet()
    
    # Create custom styles
    title_style = ParagraphStyle(
        'CustomTitle',
        parent=styles['Heading1'],
        fontSize=24,
        spaceAfter=30,
        alignment=TA_CENTER,
        textColor=colors.darkblue
    )
    
    subtitle_style = ParagraphStyle(
        'CustomSubtitle',
        parent=styles['Heading2'],
        fontSize=16,
        spaceAfter=20,
        alignment=TA_CENTER,
        textColor=colors.darkblue
    )
    
    heading1_style = ParagraphStyle(
        'CustomHeading1',
        parent=styles['Heading1'],
        fontSize=18,
        spaceBefore=20,
        spaceAfter=12,
        textColor=colors.darkblue
    )
    
    heading2_style = ParagraphStyle(
        'CustomHeading2',
        parent=styles['Heading2'],
        fontSize=14,
        spaceBefore=15,
        spaceAfter=10,
        textColor=colors.darkblue
    )
    
    heading3_style = ParagraphStyle(
        'CustomHeading3',
        parent=styles['Heading3'],
        fontSize=12,
        spaceBefore=12,
        spaceAfter=8,
        textColor=colors.darkblue
    )
    
    body_style = ParagraphStyle(
        'CustomBody',
        parent=styles['Normal'],
        fontSize=11,
        spaceAfter=12,
        alignment=TA_JUSTIFY
    )
    
    code_style = ParagraphStyle(
        'Code',
        parent=styles['Code'],
        fontSize=9,
        fontName='Courier',
        backgroundColor=colors.lightgrey,
        borderColor=colors.grey,
        borderWidth=1,
        leftIndent=12,
        rightIndent=12,
        spaceBefore=6,
        spaceAfter=6
    )
    
    # Story list to hold all content
    story = []
    
    # Split content into lines
    lines = md_content.split('\n')
    i = 0
    
    while i < len(lines):
        line = lines[i].strip()
        
        # Skip empty lines at the beginning
        if not line:
            i += 1
            continue
            
        # Title page
        if line.startswith('# ') and 'Spring Boot Forex' in line:
            story.append(Spacer(1, 2*inch))
            story.append(Paragraph(line[2:], title_style))
            i += 1
            
            # Add subtitle and metadata
            if i < len(lines) and lines[i].startswith('## '):
                story.append(Paragraph(lines[i][3:], subtitle_style))
                i += 1
            
            story.append(Spacer(1, 0.5*inch))
            
            # Add metadata
            metadata_lines = []
            while i < len(lines) and lines[i].strip().startswith('**'):
                metadata_lines.append(lines[i].strip())
                i += 1
            
            for meta_line in metadata_lines:
                story.append(Paragraph(meta_line, body_style))
            
            story.append(PageBreak())
            continue
        
        # Main headings (# )
        elif line.startswith('# '):
            story.append(PageBreak())
            story.append(Paragraph(line[2:], heading1_style))
            
        # Sub headings (## )
        elif line.startswith('## '):
            story.append(Paragraph(line[3:], heading2_style))
            
        # Sub-sub headings (### )
        elif line.startswith('### '):
            story.append(Paragraph(line[4:], heading3_style))
            
        # Code blocks
        elif line.startswith('```'):
            i += 1
            code_lines = []
            while i < len(lines) and not lines[i].strip().startswith('```'):
                code_lines.append(lines[i])
                i += 1
            
            if code_lines:
                code_text = '\n'.join(code_lines)
                # Escape XML characters for ReportLab
                code_text = code_text.replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;')
                story.append(Paragraph(f"<pre>{code_text}</pre>", code_style))
            
        # Tables
        elif '|' in line and '---' in lines[i+1] if i+1 < len(lines) else False:
            table_data = []
            
            # Header row
            headers = [cell.strip() for cell in line.split('|')[1:-1]]
            table_data.append(headers)
            
            # Skip separator line
            i += 2
            
            # Data rows
            while i < len(lines) and '|' in lines[i]:
                row = [cell.strip() for cell in lines[i].split('|')[1:-1]]
                table_data.append(row)
                i += 1
            
            # Create table
            if table_data:
                table = Table(table_data)
                table.setStyle(TableStyle([
                    ('BACKGROUND', (0, 0), (-1, 0), colors.darkblue),
                    ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
                    ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
                    ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
                    ('FONTSIZE', (0, 0), (-1, 0), 10),
                    ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
                    ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
                    ('FONTNAME', (0, 1), (-1, -1), 'Helvetica'),
                    ('FONTSIZE', (0, 1), (-1, -1), 9),
                    ('GRID', (0, 0), (-1, -1), 1, colors.black),
                ]))
                story.append(table)
                story.append(Spacer(1, 12))
            continue
            
        # Horizontal rules
        elif line.startswith('---'):
            story.append(Spacer(1, 12))
            
        # Lists
        elif line.startswith('- ') or line.startswith('* ') or re.match(r'^\d+\.', line):
            list_items = []
            while i < len(lines) and (lines[i].startswith('- ') or lines[i].startswith('* ') or re.match(r'^\d+\.', lines[i])):
                item_text = lines[i][2:] if lines[i].startswith(('- ', '* ')) else re.sub(r'^\d+\.\s*', '', lines[i])
                list_items.append(item_text)
                i += 1
            
            for item in list_items:
                story.append(Paragraph(f"• {item}", body_style))
            continue
            
        # Bold text patterns
        elif '**' in line:
            # Convert **text** to <b>text</b>
            formatted_line = re.sub(r'\*\*(.*?)\*\*', r'<b>\1</b>', line)
            story.append(Paragraph(formatted_line, body_style))
            
        # Regular paragraphs
        elif line:
            story.append(Paragraph(line, body_style))
        
        i += 1
    
    # Build PDF
    try:
        doc.build(story)
        return True
    except Exception as e:
        print(f"Error building PDF: {e}")
        return False

def main():
    """Main function to generate the PDF"""
    md_file = r"c:\java_lecture_check\PROJECT_DOCUMENTATION.md"
    pdf_file = r"c:\java_lecture_check\Spring_Boot_Forex_Project_Documentation_Professional.pdf"
    
    print("🔄 Converting Markdown to Professional PDF...")
    print(f"📖 Input:  {md_file}")
    print(f"📄 Output: {pdf_file}")
    
    if os.path.exists(md_file):
        success = create_pdf_from_markdown(md_file, pdf_file)
        
        if success and os.path.exists(pdf_file):
            file_size = os.path.getsize(pdf_file)
            size_mb = file_size / (1024 * 1024)
            print(f"✅ PDF created successfully!")
            print(f"📊 File size: {size_mb:.2f} MB")
            print(f"📁 Location: {pdf_file}")
        else:
            print("❌ Failed to create PDF")
    else:
        print(f"❌ Markdown file not found: {md_file}")

if __name__ == "__main__":
    main()
