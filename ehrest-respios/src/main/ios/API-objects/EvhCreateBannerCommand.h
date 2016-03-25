//
// EvhCreateBannerCommand.h
// generated at 2016-03-25 15:57:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBannerScope.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateBannerCommand
//
@interface EvhCreateBannerCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* appid;

// item type EvhBannerScope*
@property(nonatomic, strong) NSMutableArray* scopes;

@property(nonatomic, copy) NSString* bannerLocation;

@property(nonatomic, copy) NSString* bannerGroup;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* vendorTag;

@property(nonatomic, copy) NSString* posterPath;

@property(nonatomic, copy) NSNumber* actionType;

@property(nonatomic, copy) NSString* actionData;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* order;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

