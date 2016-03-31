//
// EvhUpdatePersonnelsToDepartment.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePersonnelsToDepartment
//
@interface EvhUpdatePersonnelsToDepartment
    : NSObject<EvhJsonSerializable>


// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* ids;

@property(nonatomic, copy) NSNumber* groupId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

