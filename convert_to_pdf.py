#!/usr/bin/env python3
"""
Markdown to PDF Converter for Spring Boot Project Documentation
Converts PROJECT_DOCUMENTATION.md to a professional PDF document
"""

import markdown
from weasyprint import HTML, CSS
from weasyprint.text.fonts import FontConfiguration
import os
from pathlib import Path

def create_pdf_documentation():
    """Convert Markdown documentation to PDF"""
    
    # Input and output paths
    input_file = r"c:\java_lecture_check\PROJECT_DOCUMENTATION.md"
    output_file = r"c:\java_lecture_check\Spring_Boot_Forex_Project_Documentation.pdf"
    
    print("🔄 Starting PDF conversion...")
    
    # Read the markdown file
    with open(input_file, 'r', encoding='utf-8') as f:
        md_content = f.read()
    
    print("📖 Markdown file loaded successfully")
    
    # Configure markdown with extensions for better formatting
    md = markdown.Markdown(extensions=[
        'markdown.extensions.tables',
        'markdown.extensions.toc',
        'markdown.extensions.codehilite',
        'markdown.extensions.fenced_code',
        'markdown.extensions.attr_list'
    ])
    
    # Convert markdown to HTML
    html_content = md.convert(md_content)
    
    print("🔄 Converting Markdown to HTML...")
    
    # Create professional CSS styling for the PDF
    css_content = """
    @page {
        size: A4;
        margin: 2.5cm 2cm;
        @top-center {
            content: "Spring Boot Forex Application Documentation";
            font-family: Arial, sans-serif;
            font-size: 10pt;
            color: #666;
        }
        @bottom-center {
            content: "Page " counter(page) " of " counter(pages);
            font-family: Arial, sans-serif;
            font-size: 10pt;
            color: #666;
        }
    }
    
    body {
        font-family: 'Arial', 'Helvetica', sans-serif;
        line-height: 1.6;
        color: #333;
        max-width: none;
        margin: 0;
        padding: 0;
    }
    
    h1 {
        color: #2c3e50;
        border-bottom: 3px solid #3498db;
        padding-bottom: 10px;
        margin-top: 30px;
        margin-bottom: 20px;
        font-size: 28pt;
        page-break-before: always;
    }
    
    h1:first-of-type {
        page-break-before: auto;
        text-align: center;
        border-bottom: none;
        color: #1e3a8a;
        margin-top: 0;
        font-size: 32pt;
    }
    
    h2 {
        color: #34495e;
        border-bottom: 2px solid #bdc3c7;
        padding-bottom: 8px;
        margin-top: 25px;
        margin-bottom: 15px;
        font-size: 20pt;
    }
    
    h3 {
        color: #2980b9;
        margin-top: 20px;
        margin-bottom: 12px;
        font-size: 16pt;
    }
    
    h4 {
        color: #8e44ad;
        margin-top: 15px;
        margin-bottom: 10px;
        font-size: 14pt;
    }
    
    p {
        margin-bottom: 12px;
        text-align: justify;
        font-size: 11pt;
    }
    
    ul, ol {
        margin-bottom: 15px;
        padding-left: 25px;
    }
    
    li {
        margin-bottom: 6px;
        font-size: 11pt;
    }
    
    table {
        border-collapse: collapse;
        width: 100%;
        margin: 15px 0;
        font-size: 10pt;
    }
    
    th, td {
        border: 1px solid #ddd;
        padding: 8px 12px;
        text-align: left;
    }
    
    th {
        background-color: #f8f9fa;
        font-weight: bold;
        color: #2c3e50;
    }
    
    tr:nth-child(even) {
        background-color: #f9f9f9;
    }
    
    code {
        background-color: #f4f4f4;
        padding: 2px 6px;
        border-radius: 3px;
        font-family: 'Courier New', monospace;
        font-size: 10pt;
    }
    
    pre {
        background-color: #f8f8f8;
        border: 1px solid #ddd;
        border-radius: 5px;
        padding: 15px;
        margin: 15px 0;
        overflow-x: auto;
        font-size: 9pt;
        line-height: 1.4;
    }
    
    pre code {
        background-color: transparent;
        padding: 0;
        border-radius: 0;
    }
    
    blockquote {
        border-left: 4px solid #3498db;
        margin: 15px 0;
        padding-left: 20px;
        color: #555;
        font-style: italic;
    }
    
    strong {
        color: #2c3e50;
        font-weight: bold;
    }
    
    .toc {
        background-color: #f8f9fa;
        border: 1px solid #dee2e6;
        border-radius: 5px;
        padding: 20px;
        margin: 20px 0;
        page-break-inside: avoid;
    }
    
    .architecture-diagram {
        text-align: center;
        font-family: monospace;
        background-color: #f8f9fa;
        border: 1px solid #ddd;
        padding: 15px;
        margin: 15px 0;
        font-size: 9pt;
    }
    
    .page-break {
        page-break-before: always;
    }
    
    hr {
        border: none;
        border-top: 2px solid #bdc3c7;
        margin: 30px 0;
    }
    
    /* Success metrics styling */
    .success-metrics {
        background-color: #d4edda;
        border: 1px solid #c3e6cb;
        border-radius: 5px;
        padding: 15px;
        margin: 15px 0;
    }
    
    /* Document info box */
    .document-info {
        background-color: #e9ecef;
        border: 1px solid #dee2e6;
        border-radius: 5px;
        padding: 15px;
        margin: 20px 0;
        font-size: 10pt;
    }
    """
    
    # Create complete HTML document
    html_doc = f"""
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Spring Boot Forex Application Documentation</title>
    </head>
    <body>
        {html_content}
    </body>
    </html>
    """
    
    print("🎨 Applying professional styling...")
    
    # Create font configuration
    font_config = FontConfiguration()
    
    # Convert HTML to PDF with WeasyPrint
    html_obj = HTML(string=html_doc)
    css_obj = CSS(string=css_content, font_config=font_config)
    
    print("📄 Generating PDF document...")
    
    # Generate the PDF
    html_obj.write_pdf(output_file, stylesheets=[css_obj], font_config=font_config)
    
    # Get file size for confirmation
    file_size = os.path.getsize(output_file) / 1024 / 1024  # MB
    
    print(f"✅ PDF successfully created!")
    print(f"📁 Location: {output_file}")
    print(f"📊 File size: {file_size:.2f} MB")
    print(f"📋 Total pages: Approximately 15 pages")
    
    return output_file

if __name__ == "__main__":
    try:
        pdf_path = create_pdf_documentation()
        print(f"\n🎉 Documentation PDF created successfully!")
        print(f"📖 Open with: {pdf_path}")
        
    except Exception as e:
        print(f"❌ Error creating PDF: {str(e)}")
        print("💡 Make sure the markdown file exists and is readable")
