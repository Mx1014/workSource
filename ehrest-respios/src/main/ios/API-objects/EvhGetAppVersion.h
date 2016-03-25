//
// EvhGetAppVersion.h
// generated at 2016-03-25 17:08:12 
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

