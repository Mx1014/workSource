//
// EvhUpdateBannerAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateBannerAdminCommand
//
@interface EvhUpdateBannerAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* posterPath;

@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSNumber* scopeId;

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

