//
// EvhLaunchPadLayoutJson.h
// generated at 2016-03-25 15:57:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhLaunchPadLayoutGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLaunchPadLayoutJson
//
@interface EvhLaunchPadLayoutJson
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* versionCode;

@property(nonatomic, copy) NSString* versionName;

@property(nonatomic, copy) NSString* layoutName;

@property(nonatomic, copy) NSString* displayName;

// item type EvhLaunchPadLayoutGroupDTO*
@property(nonatomic, strong) NSMutableArray* groups;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

