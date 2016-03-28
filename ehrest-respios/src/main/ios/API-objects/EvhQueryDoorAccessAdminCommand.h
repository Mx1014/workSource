//
// EvhQueryDoorAccessAdminCommand.h
// generated at 2016-03-25 19:05:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorAccessAdminCommand
//
@interface EvhQueryDoorAccessAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSString* search;

@property(nonatomic, copy) NSNumber* linkStatus;

@property(nonatomic, copy) NSNumber* doorType;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

