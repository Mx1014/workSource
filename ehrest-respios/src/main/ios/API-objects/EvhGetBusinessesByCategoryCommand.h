//
// EvhGetBusinessesByCategoryCommand.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBusinessesByCategoryCommand
//
@interface EvhGetBusinessesByCategoryCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

