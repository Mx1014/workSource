//
// EvhUpdateQualityStandardCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhStandardGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateQualityStandardCommand
//
@interface EvhUpdateQualityStandardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* standardNumber;

@property(nonatomic, copy) NSString* description_;

// item type EvhStandardGroupDTO*
@property(nonatomic, strong) NSMutableArray* group;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

