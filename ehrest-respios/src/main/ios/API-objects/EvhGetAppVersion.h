//
// EvhGetAppVersion.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetAppVersion
//
@interface EvhGetAppVersion
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* downloadLink;

@property(nonatomic, copy) NSString* versionCode;

@property(nonatomic, copy) NSString* versionName;

@property(nonatomic, copy) NSString* versionDescLink;

@property(nonatomic, copy) NSNumber* operation;

@property(nonatomic, copy) NSNumber* mktDataVersion;

@property(nonatomic, copy) NSNumber* userLocRptFreq;

@property(nonatomic, copy) NSNumber* userContactRptFreq;

@property(nonatomic, copy) NSNumber* userRptConfigVersion;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

