# TensorModule
Inependent project containing tensor model for image-to-image generative AI. || Used in UStitchApp project, linked through pom file.
# TensorModel

**Independent Java project containing a tensor model for image-to-image generative AI**

## Description
TensorModel is a standalone project that provides a **reusable AI model** for image-to-image generation.  
It is designed to be **integrated into other projects**, such as the **UStitchApp**, to generate embroidery patterns or similar images.  

## Features
- AI-based **image-to-image generation**  
- Modular and reusable in multiple Java projects  
- Easy to integrate via **Maven dependency** (pom.xml)  

## Tech Stack
- Java 17  
- TensorFlow (for model implementation)  
- Maven  

## Integration
To use TensorModel in another project:

1. Add the TensorModel project as a **Maven dependency** in your `pom.xml` file, or  
2. Include the project as a module in your workspace/IDE

## How to Use
TensorModel is **not meant to run standalone**. To use it:  

1. Clone the repo:  
```bash
git clone https://github.com/YOUR_USERNAME/TensorModel.git


Example (in UStitchApp):  
```xml
<dependency>
    <groupId>com.yourusername</groupId>
    <artifactId>TensorModel</artifactId>
    <version>1.0.0</version>
</dependency>

