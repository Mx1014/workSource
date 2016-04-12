//
// EvhListPropOwnerCommand.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropOwnerCommand
//
@interface EvhListPropOwnerCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

