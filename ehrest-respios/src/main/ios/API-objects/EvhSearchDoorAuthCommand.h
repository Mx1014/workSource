//
// EvhSearchDoorAuthCommand.h
// generated at 2016-03-31 20:15:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchDoorAuthCommand
//
@interface EvhSearchDoorAuthCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

