//
// EvhUpdatePropOwnerCommand.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePropOwnerCommand
//
@interface EvhUpdatePropOwnerCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSString* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

