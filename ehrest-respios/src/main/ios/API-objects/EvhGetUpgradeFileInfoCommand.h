//
// EvhGetUpgradeFileInfoCommand.h
// generated at 2016-04-22 13:56:47 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUpgradeFileInfoCommand
//
@interface EvhGetUpgradeFileInfoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* versionCode;

@property(nonatomic, copy) NSNumber* packageEdition;

@property(nonatomic, copy) NSNumber* devicePlatform;

@property(nonatomic, copy) NSNumber* distributionChannel;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSString* jsonParams;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

