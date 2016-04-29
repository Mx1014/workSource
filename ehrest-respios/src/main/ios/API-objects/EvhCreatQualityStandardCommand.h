//
// EvhCreatQualityStandardCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRepeatSettingsDTO.h"
#import "EvhStandardGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatQualityStandardCommand
//
@interface EvhCreatQualityStandardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* standardNumber;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, strong) EvhRepeatSettingsDTO* repeat;

// item type EvhStandardGroupDTO*
@property(nonatomic, strong) NSMutableArray* group;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

