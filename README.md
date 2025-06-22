# CereBro 🧠
## A Cloud-Integrated Android Application for Cognitive Disorder Screening

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![TensorFlow Lite](https://img.shields.io/badge/ML-TensorFlow%20Lite-orange.svg)](https://www.tensorflow.org/lite)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow.svg)](https://firebase.google.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)

CereBro is an innovative mobile application that leverages machine learning and cloud technology to provide accessible, real-time screening for neurological disorders including Autism Spectrum Disorder (ASD), Dyslexia, and Dementia. Unlike traditional diagnostic methods that require clinical visits, CereBro brings AI-powered screening directly to users' smartphones.

## 🌟 Key Features

### Multi-Disorder Screening
- **Autism Detection**: Deep learning model analyzing behavioral patterns from questionnaire responses
- **Dyslexia Assessment**: Text pattern recognition identifying linguistic markers and reading difficulties  
- **Dementia Screening**: Gamified cognitive assessments based on MMSE (Mini-Mental State Examination)

### Advanced Technology Stack
- **On-Device Processing**: TensorFlow Lite models for offline inference and low-latency predictions
- **Cloud Integration**: Firebase services for real-time model updates and data synchronization
- **Hybrid Architecture**: Combines edge computing with cloud-based continuous learning

### User-Centric Design
- **Gamification**: Interactive cognitive games for engaging dementia screening
- **Offline Capability**: Core functionality available without internet connection
- **Real-Time Results**: Instant feedback and personalized reports
- **Accessibility**: Designed for users in remote areas with limited healthcare access

## 🏗️ System Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Mobile App    │    │  Firebase Cloud  │    │  Cloud Backend  │
│                 │    │    Services      │    │                 │
│ • TFLite Models │◄──►│ • Model Sync     │◄──►│ • ML Models     │
│ • Local Storage │    │ • Data Storage   │    │ • Model Training│
│ • Game Engine   │    │ • Authentication │    │ • Updates       │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

## 📱 Application Modules

### 1. Autism Screening Module
- **Technology**: Deep Neural Networks with TensorFlow Lite
- **Input**: Behavioral questionnaire responses
- **Accuracy**: 78% on validation dataset
- **Features**: Offline inference, pattern recognition in behavioral data

### 2. Dyslexia Analysis Module  
- **Technology**: Natural Language Processing & Pattern Recognition
- **Input**: Text samples and linguistic analysis
- **Detection**: Letter reversals, misspellings, inconsistent phrasing
- **Features**: Real-time text analysis, linguistic pattern identification

### 3. Dementia Assessment Module
- **Technology**: Game-based cognitive evaluation
- **Input**: Interactive memory and cognitive tasks
- **Engagement**: 85% task completion rate
- **Features**: MMSE-based assessments, gamified user experience

## 🚀 Getting Started

### Prerequisites
- Android Studio 4.0+
- Android SDK API level 21+
- Firebase account
- TensorFlow Lite dependencies

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/cerebro.git
   cd cerebro
   ```

2. **Set up Firebase**
   - Create a new Firebase project
   - Add your Android app to the project
   - Download `google-services.json` and place it in the `app/` directory

3. **Configure dependencies**
   ```gradle
   // Add to app/build.gradle
   implementation 'org.tensorflow:tensorflow-lite:2.12.0'
   implementation 'com.google.firebase:firebase-firestore:24.7.1'
   implementation 'com.google.firebase:firebase-ml-model-downloader:24.1.2'
   ```

4. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

### Configuration

1. **Firebase Setup**
   - Enable Firestore Database
   - Configure Firebase ML Model hosting
   - Set up authentication (optional)

2. **Model Integration**
   - Place TFLite models in `assets/` directory
   - Configure model loading in `ModelManager.kt`
   - Set up cloud model synchronization

## 📊 Performance Metrics

| Feature | Performance |
|---------|-------------|
| **Response Time** | Real-time with minimal delays |
| **Offline Functionality** | Full autism screening available offline |
| **Model Updates** | Seamless cloud-based updates |
| **User Engagement** | 85% completion rate for cognitive tasks |
| **Accuracy** | 78% for autism detection model |

## 🔬 Technical Implementation

### Machine Learning Pipeline
```kotlin
// Example: Loading TFLite model
class AutismPredictor {
    private lateinit var interpreter: Interpreter
    
    fun loadModel(context: Context) {
        val model = loadModelFile(context, "autism_model.tflite")
        interpreter = Interpreter(model)
    }
    
    fun predict(input: FloatArray): Float {
        val output = Array(1) { FloatArray(1) }
        interpreter.run(input, output)
        return output[0][0]
    }
}
```

### Cloud Integration
```kotlin
// Firebase model synchronization
class ModelSyncManager {
    fun syncModelsFromCloud() {
        FirebaseModelDownloader.getInstance()
            .getModel("autism_model", DownloadType.LATEST_MODEL,
                CustomModelDownloadConditions.Builder().build())
            .addOnSuccessListener { model ->
                // Update local model
                updateLocalModel(model)
            }
    }
}
```

## 🎮 Gamification Features

### Dementia Screening Games
- **Memory Pattern Game**: Tests short-term memory and pattern recognition
- **Word Association**: Evaluates cognitive flexibility and language processing
- **Spatial Navigation**: Assesses spatial memory and orientation
- **Attention Tasks**: Measures sustained attention and concentration

### Engagement Metrics
- Progress tracking and achievement system
- Adaptive difficulty based on user performance
- Visual feedback and encouraging messaging
- Completion rewards and milestones

## 📈 Research & Validation

### Clinical Relevance
- Based on established diagnostic criteria (DSM-5, MMSE)
- Validated against known behavioral and cognitive patterns
- Designed to complement, not replace, professional diagnosis

### Research Contributions
- **Novel Approach**: First cloud-integrated mobile app for multi-disorder screening
- **Real-World Deployment**: Addresses the gap between research models and practical applications
- **Continuous Learning**: Models improve over time through federated learning
- **Accessibility**: Makes AI-based screening available in underserved areas

## 🛠️ Development Roadmap

### Current Status
- ✅ Core application development complete
- ✅ TensorFlow Lite integration implemented
- ✅ Firebase cloud services configured
- ✅ Basic UI/UX design finalized

### Upcoming Features
- 🔄 **Enhanced Model Accuracy**: Larger, more diverse training datasets
- 🌍 **Multilingual Support**: Expand accessibility globally
- 👥 **Clinical Validation**: Large-scale trials with medical professionals
- ⚡ **Performance Optimization**: Better support for low-end devices
- 📱 **Wearable Integration**: Eye-tracking, voice analysis, EEG sensors

### Future Enhancements
- Advanced federated learning implementation
- Integration with electronic health records
- Telemedicine platform connectivity
- Advanced analytics and reporting dashboard

## 📚 Research Paper & Citations

This project is based on the research paper:
**"CereBro: A Cloud-Integrated Android Application for Cognitive Disorder Screening"**

### Authors
- Thrilochan Reddy Vemula (Lead Developer)
- C. Sruthi (Assistant Professor, Project Supervisor)
- Chetan Aditya Lakka (Developer)
- Dr. Mohan Dholvan (Professor, Technical Advisor)
- Bharath Chandra Ganji (Developer)

*Sreenidhi Institute of Science and Technology, Hyderabad, India*

## 🤝 Contributing

We welcome contributions from the community! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Ways to Contribute
- 🐛 Bug reports and fixes
- 💡 Feature suggestions and implementations  
- 📖 Documentation improvements
- 🧪 Testing and validation
- 🌍 Localization and accessibility improvements

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ⚠️ Disclaimer

**Important**: CereBro is designed as a screening tool to support early detection and is not intended to replace professional medical diagnosis. Always consult with qualified healthcare professionals for proper medical evaluation and treatment.

## 📞 Contact & Support

- **Email**: 21311A1908@sreenidhi.edu.in
- **Institution**: Sreenidhi Institute of Science and Technology
- **Location**: Hyderabad, India

For technical support or research collaboration inquiries, please open an issue on GitHub or contact the development team directly.

---

<div align="center">
  <strong>Making AI-powered healthcare screening accessible to everyone, everywhere.</strong>
</div>
