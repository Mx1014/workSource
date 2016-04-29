//
// EvhSetVideoConfAccountPreferentialRuleCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:51 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetVideoConfAccountPreferentialRuleCommand
//
@interface EvhSetVideoConfAccountPreferentialRuleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* limit;

@property(nonatomic, copy) NSString* subtract;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

