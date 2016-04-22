//
// EvhListActivitiesByLocationCommand.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGeoLocation.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListActivitiesByLocationCommand
//
@interface EvhListActivitiesByLocationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

// item type EvhGeoLocation*
@property(nonatomic, strong) NSMutableArray* locationPointList;

@property(nonatomic, copy) NSNumber* scope;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

